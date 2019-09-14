import { Injectable } from '@angular/core';
import { AvailabilityOutModel } from '../models/availability.model';
import { SearchService } from '../services/search.service';
import { FilterModel } from '../models/filters.model';

@Injectable({
  providedIn: 'root',
})

export class FilterService {

    public maxPrice: {value: number, currency: string};
    public filterInitParams: FilterModel = {
      fare: {
        saver: true,
        standard: true,
        flex: true,
        bizstd: true
      },
      price: {
        max: 999999
      },
      departure: {
        min: 0,
        max: 24
      },
      return: {
        min: 0,
        max: 24
      }
    };

    constructor(private searchService: SearchService) {
    }

    // calculate max price
    calculateMaxPrice(data) {
      let priceFirst = [];
      for (let first of data) {
        priceFirst.push(first[0]);
      }
      const max = priceFirst.reduce(function (prev, current) {
         return (prev.totalPrice.price > current.totalPrice.price) ? prev : current
      });

      this.maxPrice = {value: max.totalPrice.price, currency: max.totalPrice.currency};
    }

    // filter results
    checkFilters(data, filter) {
      // fare type
      for (let f in filter.fare) {
        if (!filter.fare[f]) {
          if (data.outbound.fareType === f.toUpperCase()
            || data.inbound.fareType === f.toUpperCase()) {
              return false;
          }
        }
      }
      // price
      if (filter.price.max && data.totalPrice.price > filter.price.max) {
        return false;
      }
      // departure time
      if (filter.departure.min) {
        let departureDate = new Date(data.outbound.segments[0].departureDate)
        if (departureDate.getHours() < parseInt(filter.departure.min)) {
          return false;
        }
      }
      if (filter.departure.max) {
        let departureDate = new Date(data.outbound.segments[0].departureDate)
        if (departureDate.getHours() >= parseInt(filter.departure.max)) {
          return false;
        }
      }
      // return time
      if (filter.return.min) {
        let returnDate = new Date(data.inbound.segments[0].departureDate)
        if (returnDate.getHours() < parseInt(filter.return.min)) {
          return false;
        }
      }
      if (filter.return.max) {
        let returnDate = new Date(data.inbound.segments[0].departureDate)
        if (returnDate.getHours() >= parseInt(filter.return.max)) {
          return false;
        }
      }
      return true;
    }
}
