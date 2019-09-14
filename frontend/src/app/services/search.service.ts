import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RestService } from '../services/rest.service';
import { AuthService } from '../services/auth.service';
import { AvailabilityInModel, AvailabilityOutModel, BookerProxyModel } from '../models/availability.model';

@Injectable({
  providedIn: 'root',
})

export class SearchService {

    public out: null | AvailabilityOutModel;
    public proxyParams: BookerProxyModel;
    public searchProgress: boolean = false;

    constructor(private restService: RestService, private authService: AuthService) {
    }

    searchFlights(body: AvailabilityInModel): Observable<AvailabilityOutModel> {
      if (this.authService.accessToken) {
        return this.restService.post(`booking/availability`, body, 'application/json', this.authService.accessToken);
      }
    }

}
