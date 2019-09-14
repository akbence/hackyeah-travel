import { Component } from '@angular/core';
import { DatePipe } from '@angular/common';
import { HistoryService } from '../../services/history.service';
import { SearchService } from '../../services/search.service';
import { CommonService } from '../../services/common.service';
import { ConfigService } from '../../services/config.service';
import { AvailabilityInModel } from '../../models/availability.model';

@Component({
  selector: 'content-history',
  templateUrl: './history.template.html',
  styleUrls: ['./content.style.scss']
})

export class HistoryComponent {

  public history = [];

  constructor(
    public historyService: HistoryService,
    public commonService: CommonService,
    private searchService: SearchService,
    private configService: ConfigService){}

  readHistory() {
    this.history = this.historyService.getSearch();
  }

  makeDate(dateString: string) {
    return this.commonService.makeISODate(dateString);
  }

  removeSearch(index: number) {
    this.historyService.removeItem(index);
    this.readHistory();
  }

  searchFromHistory(index: number) {
    this.commonService.sendMessage({
      selectedTabIndex: 0,
      searchParams: this.history[index]
    });
  }

  cityImage(code: string) {
    let city = 'lot.png';

    if (['LON', 'LHR', 'LCY'].indexOf(code) !== -1) {
      city = 'london.png';
    }
    else if (['JFK'].indexOf(code) !== -1) {
      city = 'newyork.png';
    }
    else if (['CDG'].indexOf(code) !== -1) {
      city = 'paris.png';
    }
    else if (['WAW'].indexOf(code) !== -1) {
      city = 'warsaw.png';
    }
    else if (['ORD'].indexOf(code) !== -1) {
      city = 'chicago.png';
    }
    else if (['LAX'].indexOf(code) !== -1) {
      city = 'losangeles.png';
    }

    return `url(${this.configService.staticUrl}/assets/images/cities/${city})`;
  }
}
