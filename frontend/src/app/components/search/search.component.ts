import { Component } from '@angular/core';
import { SearchService } from '../../services/search.service';
import { FilterService } from '../../services/filter.service';
import { ConfigService } from '../../services/config.service';
import { FilterModel } from '../../models/filters.model';

@Component({
  selector: 'search-results',
  templateUrl: './search.template.html',
  styleUrls: ['./search.style.scss']
})

export class SearchComponent {

  public filterParams: FilterModel;

  constructor(
    public searchService: SearchService,
    private filterService: FilterService,
    private configService: ConfigService) {
      this.filterParams = this.filterService.filterInitParams;
  }

  calculateDuration(duration: number): string {
    let minutes = 0;
    let hours = 0;
    let out = '';

    if (duration <= 60){
      minutes = duration;
    } else {
      hours = Math.floor(duration / 60);
      minutes = duration % 60;
    }

    out += hours ? hours + 'h ' : '';
    out += minutes ? minutes + 'm': '';

    return out;
  }

  passFilters(event) {
      this.filterParams = event;
  }

  //* filter results */
  checkFilters(data, filter) {
    return this.filterService.checkFilters(data, filter);
  }

  //* produce url for an airline logo */
  logotype(carrier: string) {
    return `url(${this.configService.staticUrl}/assets/images/${carrier}.jpg)`;
  }
}
