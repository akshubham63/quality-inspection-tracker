import { Routes } from '@angular/router';
import { InspectionListComponent } from './components/inspection-list/inspection-list.component';
import { InspectionFormComponent } from './components/inspection-form/inspection-form.component';
import { SummaryComponent } from './components/summary/summary.component';
import { LoginComponent } from './components/login/login.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/inspections', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'inspections', component: InspectionListComponent, canActivate: [authGuard] },
  { path: 'inspections/new', component: InspectionFormComponent, canActivate: [authGuard] },
  { path: 'summary', component: SummaryComponent, canActivate: [authGuard] }
];
