import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RestService } from '../services/rest.service';
import { AiportModel } from '../models/availability.model';

@Injectable({
  providedIn: 'root',
})

export class AirportsService {

  constructor(private restService: RestService) {
  }

  getAirports(): Observable<AiportModel[]>  {
      return this.restService.get(`common/airports/get`);
  }
}
