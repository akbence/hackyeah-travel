import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RestService } from '../services/rest.service';
import { SearchService } from './search.service';

@Injectable({
  providedIn: 'root',
})

export class SearchOutService {

  private data;
  public out;

  constructor(private searchService: SearchService) {
  }

  calculateOut(dataIn) {
    this.data = dataIn.data;
    this.out = [];

    if (this.data) {
      for (let offer of this.data.offers.listOffers) {
        if (offer.totalPrice) {
          this.findInbound(offer);
        }
      }
    }

    this.searchService.out = {
      data: this.out,
      status: dataIn.status,
      errors: dataIn.errors
    }

  }

  findInbound(offer) {
    for (let group of offer.groupFlights) {
      let inFlight =  this.data.listInbound.filter((bound) => {
        return bound.idFlight == group.idInBound;
      });
      let outFlight =  this.data.listOutbound.filter((bound) => {
        return bound.idFlight == group.idOutBound;
      });

      let preOut = {
        inbound: {
          duration: inFlight[0].duration,
          segments: inFlight[0].listSegment,
          fareType: offer.inFareFamily
        },
        totalPrice: {
          price: offer.totalPrice.price,
          basePrice: offer.totalPrice.basePrice,
          tax: offer.totalPrice.tax,
          currency: this.data.offers.currency
        }
      };

      if (outFlight.length > 0) {
        preOut['outbound'] = {
          duration: outFlight[0].duration,
          segments: outFlight[0].listSegment,
          fareType: offer.outFareFamily
        };
      }

      this.out.push(preOut);
    }
  }
}
