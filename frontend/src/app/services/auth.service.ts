import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RestService } from '../services/rest.service';
import { ConfigService } from '../services/config.service';
import { SessionInModel, SessionOutModel } from '../models/session.model';

@Injectable({
  providedIn: 'root',
})

export class AuthService {

  public accessToken: string | null = null;

  constructor(private configService: ConfigService, private restService: RestService) {
  }

  createToken(): Observable<SessionOutModel>  {
    let body = {
    	'secret_key': this.configService.apiCredentials.secretKey
    };
    return this.restService.post(`auth/token/get`, body);
  }

  refreshToken(currentToken: string): Observable<SessionOutModel>  {
    return this.restService.post(`auth/token/refresh`, '', 'application/json', currentToken);
  }
}
