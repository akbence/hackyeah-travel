import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class CommonService {

  public message: string;
  public failMessage: string = 'Something was wrong'

  private subject = new Subject<any>();

  constructor() {
  }

  sendMessage(message: any) {
      this.subject.next(message);
  }

  clearMessage() {
      this.subject.next();
  }

  getMessage(): Observable<any> {
      return this.subject.asObservable();
  }

  makeISODate(dateString: string) {
    let day = dateString.slice(0, 2);
    let month = dateString.slice(2, 4);
    let year = dateString.slice(4, 8);

    return (new Date(`${year}-${month}-${day}`));
  }

  set error(msg: string) {
      this.message = msg;
      setTimeout( () => {
          this.message = '';
      }, 1200)
  }
}
