import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { RootComponent } from './components/root/root.component';
import { FormComponent } from './components/form/form.component';
import { SearchComponent } from './components/search/search.component';
import { FiltersComponent } from './components/search/filters.component';
import { FooterComponent } from './components/content/footer.component';
import { HistoryComponent } from './components/content/history.component';

import { MaterialModule } from './material.module';
import { MatNativeDateModule } from '@angular/material';

@NgModule({
  declarations: [
    RootComponent, FormComponent, SearchComponent, FiltersComponent, FooterComponent, HistoryComponent
  ],
  imports: [
    BrowserModule, BrowserAnimationsModule, HttpClientModule, MaterialModule, MatNativeDateModule, FormsModule
  ],
  providers: [],
  bootstrap: [ RootComponent ]
})

export class AppModule { }
