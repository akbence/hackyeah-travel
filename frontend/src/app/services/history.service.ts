import { Injectable } from '@angular/core';
import { AvailabilityInParamsModel } from '../models/availability.model';

@Injectable({
  providedIn: 'root',
})

export class HistoryService {

  private storageName: string = 'historySearch';
  private maxItems: number = 9;
  constructor() {}

  // save search params
  saveSearch(parameters: AvailabilityInParamsModel) {
    let history = localStorage.getItem(this.storageName) || '[]';
    let historyJson = JSON.parse(history);
    let params = JSON.stringify(parameters);
    if (history.indexOf(params) === -1) {
      historyJson.unshift(parameters);
      localStorage.setItem(this.storageName, JSON.stringify(historyJson));
    }
    if (historyJson.lenght >= this.maxItems) {
      this.removeItem(historyJson.lenght - 1);
    }
  }

  // get search params
  getSearch() {
    return JSON.parse( localStorage.getItem(this.storageName) );
  }

  // remove item from history
  removeItem(index: number): void {
    let storage = this.getSearch();
    storage.splice(index, 1);
    localStorage.removeItem(this.storageName);
    for (let item of storage) {
      this.saveSearch(item);
    }
  }

  // nie zapisuj takich samych wyszukiwan
}
