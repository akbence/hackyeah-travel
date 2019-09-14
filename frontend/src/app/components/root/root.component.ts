import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { CommonService } from '../../services/common.service';
import { SessionInModel, SessionOutModel } from '../../models/session.model';
import { HistoryComponent } from '../content/history.component';

@Component({
  selector: 'app-root',
  templateUrl: './root.template.html',
  styleUrls: ['./root.style.scss']
})

export class RootComponent implements OnInit, OnDestroy {

  @ViewChild(HistoryComponent) childHistory: HistoryComponent;

  public selectedTabIndex: number = 0;
  private subscription: Subscription;

  constructor(private authService: AuthService, public commonService: CommonService) {
    this.subscription = this.commonService.getMessage().subscribe(message => {
      if (message.selectedTabIndex !== 'undefined') {
        this.selectedTabIndex = message.selectedTabIndex;
      }
    });
  }

  ngOnInit() {
    this.getToken();
  }

  getToken() {
    this.authService
      .createToken()
      .subscribe(
         (ret: SessionOutModel) => {
           this.authService.accessToken = ret.access_token;
           this.refreshTokenPeriod(ret.expires_in);
         },
         error => {
           console.log('API problem');
         }
     );
  }

  refreshToken() {
    this.authService
      .refreshToken(this.authService.accessToken)
      .subscribe(
         (ret: SessionOutModel) => {
           this.authService.accessToken = ret.access_token;
           this.refreshTokenPeriod(ret.expires_in);
         },
         error => {
           console.log('API problem');
         }
     );
  }

  refreshTokenPeriod(expiresIn) {
    setTimeout( () => {
      this.refreshToken();
    }, (expiresIn - 30) * 1000);
  }

  changedTab(event) {
    if (event.tab.textLabel == 'History') {
      this.childHistory.readHistory();
    }
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
