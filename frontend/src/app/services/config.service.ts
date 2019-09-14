import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})

export class ConfigService {

    public apiServer: string;
    public staticUrl: string;
    public apiCredentials: {
      apiKey: string,
      secretKey: string
    };

    constructor() {
      this.apiServer = environment.server;
      this.apiCredentials = environment.apiCredentials;
      this.staticUrl = environment.staticUrl;
    }

}
