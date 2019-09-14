import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { ConfigService } from '../services/config.service';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';

@Injectable({
    providedIn: 'root',
})

export class RestService {

    constructor(private http: HttpClient, private configService: ConfigService) {
    }

    // GET method
    get(service: string): Observable<any> {

      let headers = {
        'Content-Type': 'application/json',
        'X-Api-Key': this.configService.apiCredentials.apiKey
      };

      const httpOptions = {
          headers: new HttpHeaders(headers)
      };

      return this.http
          .get(`${this.configService.apiServer}${service}`, httpOptions)
          .pipe(
            retry(1),
            catchError(this.handleError)
          );
    }

    // POST method
    post(service: string, body: any, type: string = 'application/json', bearerToken?: string): Observable<any> {

      let headers = {
        'Content-Type': type,
        'X-Api-Key': this.configService.apiCredentials.apiKey
      };

      if (bearerToken) {
        headers['Authorization'] = 'Bearer ' + bearerToken;
      }

      const httpOptions = {
          headers: new HttpHeaders(headers)
      };

      return this.http
          .post(`${this.configService.apiServer}${service}`, body, httpOptions)
          .pipe(
              retry(1),
              catchError(this.handleError)
          );

    }

    private handleError(error: HttpErrorResponse) {
        return throwError(error);
    }

}
