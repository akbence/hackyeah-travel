import { Component, OnInit, OnDestroy } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Observable, Subscription } from 'rxjs';
import { CommonService } from '../../services/common.service';
import { AuthService } from '../../services/auth.service';
import { SearchService } from '../../services/search.service';
import { FilterService } from '../../services/filter.service';
import { HistoryService } from '../../services/history.service';
import { AirportsService } from '../../services/airports.service';
import { SessionInModel, SessionOutModel } from '../../models/session.model';
import { AvailabilityInModel, AvailabilityInParamsModel,
         AvailabilityOutModel, BookerProxyModel, AiportModel } from '../../models/availability.model';

@Component({
  selector: 'search-form',
  templateUrl: './form.template.html',
  styleUrls: ['./form.style.scss']
})

export class FormComponent implements OnInit, OnDestroy {

  public errorMessage: string = '';
  public aiports$: Observable<AiportModel[]>;
  private subscription: Subscription;

  public passengersLimit: number[] = [1, 2, 3, 4, 5, 6, 7, 8, 9];
  public minDateFrom: Date;
  public minDateTo: Date;
  public portFrom: string;
  public portTo: string;
  public dF: Date;
  public dT: Date;
  public adt: number = 1;
  public class: 'E'|'F'|'B' = 'E';
  public tripType: 'R'|'O' = 'R';
  public market: string = 'PL';
  public searchParams: AvailabilityInModel;
  public chd: number = 0;
  public c14: number = 0;
  public inf: number = 0;

  constructor(
    public authService: AuthService,
    public commonService: CommonService,
    public searchService: SearchService,
    private filterService: FilterService,
    private historyService: HistoryService,
    private airportsService: AirportsService) {
      this.minDateFrom = new Date();
      this.minDateTo = new Date();
      this.subscription = this.commonService.getMessage().subscribe(message => {
        if (message.searchParams !== 'undefined') {
          this.readParamsFromHistory(message.searchParams);
          setTimeout(() => {
            this.searchFlights();
          }, 380);
        }
      });
  }

  ngOnInit() {
    this.aiports$ = this.airportsService.getAirports();
  }

  clickSearch() {

    let datePipe = new DatePipe('en-US');

    this.searchParams = {
      params: {
        cabinClass: this.class,
        market: this.market,
        departureDate: [datePipe.transform(this.dF, 'ddMMyyy')],
        returnDate: datePipe.transform(this.dT, 'ddMMyyy'),
        origin: [this.portFrom],
        tripType: this.tripType,
        adt: this.adt,
        destination: [this.portTo]
      }
    }

    this.searchFlights();
  }

  searchFlights() {
    this.searchService.searchProgress = true;
    this.searchService.out = null;
    this.errorMessage = '';
    this.searchService
      .searchFlights(this.searchParams)
      .subscribe(
         (ret: AvailabilityOutModel) => {
           if (ret.data.length > 0) {
             this.searchService.out = ret;
             this.filterService.calculateMaxPrice(ret.data);
             this.historyService.saveSearch(this.searchParams.params);
           } else {
             this.errorMessage = 'No results found.';
           }
         },
         err => {
           if (err.status == 400) {
             this.errorMessage = 'No results found.';
           } else if (err.error.code === 41211 || err.error.code === 41866) {
             this.errorMessage = 'No results for selected dates.';
           } else {
             this.errorMessage = 'Something went wrong :(';
           }
         }
      )
      .add(() => {
        this.searchService.searchProgress = false;
      });
  }

  readParamsFromHistory(searchParams: AvailabilityInParamsModel) {
    this.searchParams = {
      params: searchParams,
      options: {fromCache: true}
    };
    this.portFrom = searchParams.origin[0];
    this.portTo = searchParams.destination[0];
    this.dF = this.commonService.makeISODate( searchParams.departureDate[0] );
    this.dT = this.commonService.makeISODate( searchParams.returnDate );
    this.adt = searchParams.adt;
    this.class = searchParams.cabinClass;
    this.tripType = searchParams.tripType;
    this.market = searchParams.market;
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
