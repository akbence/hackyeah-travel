import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { FilterService } from '../../services/filter.service';
import { FilterModel } from '../../models/filters.model';

@Component({
  selector: 'search-filters',
  templateUrl: './filters.template.html',
  styleUrls: ['./filters.style.scss']
})

export class FiltersComponent implements OnInit {

  @Output() filters = new EventEmitter();
  @Input() filterParams: FilterModel;
  public hours: Date[] = [];
  private initPriceMax: number;
  public currency: string;

  constructor(public filterService: FilterService) {
    for (let i = 0; i <= 24; i++) {
      this.hours.push(new Date(new Date().setHours(i)));
    }
  }

  ngOnInit() {
    this.initPriceMax = this.filterParams.price.max;
  }

  emitFilters() {
    this.filters.emit( this.filterParams );
  }

  showMaxPrice() {
    let max = this.filterService.maxPrice.value;
    if (this.filterParams.price.max !== this.initPriceMax) {
      max = this.filterParams.price.max;
    }
    return `${max} ${this.filterService.maxPrice.currency}`;
  }
}
