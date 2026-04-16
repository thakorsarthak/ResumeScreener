import { Routes } from '@angular/router';
import { ScreenerComponent } from './features/screener/screener.component';
import { HistoryComponent } from './features/history/history.component';

export const routes: Routes = [
    { path: '', redirectTo: 'screen', pathMatch: 'full' },
  { path: 'screen', component: ScreenerComponent },
  { path: 'history', component: HistoryComponent }
];
