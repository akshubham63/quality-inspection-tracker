import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import {
  Inspection,
  InspectionRequest,
  ResolveRequest,
  Summary,
  ApiResponse,
  Severity,
  Status
} from '../models/inspection.model';

@Injectable({
  providedIn: 'root'
})
export class InspectionService {
  private readonly apiUrl = '/api/inspections';

  constructor(private http: HttpClient) {}

  getInspections(filters?: {
    severity?: Severity;
    status?: Status;
    startDate?: string;
    endDate?: string;
    sortBy?: string;
    sortDirection?: string;
  }): Observable<Inspection[]> {
    let params = new HttpParams();
    
    if (filters) {
      if (filters.severity) {
        params = params.set('severity', filters.severity);
      }
      if (filters.status) {
        params = params.set('status', filters.status);
      }
      if (filters.startDate) {
        params = params.set('startDate', filters.startDate);
      }
      if (filters.endDate) {
        params = params.set('endDate', filters.endDate);
      }
      if (filters.sortBy) {
        params = params.set('sortBy', filters.sortBy);
      }
      if (filters.sortDirection) {
        params = params.set('sortDirection', filters.sortDirection);
      }
    }

    return this.http.get<ApiResponse<Inspection[]>>(this.apiUrl, { params })
      .pipe(map(response => response.data));
  }

  getInspectionById(id: number): Observable<Inspection> {
    return this.http.get<ApiResponse<Inspection>>(`${this.apiUrl}/${id}`)
      .pipe(map(response => response.data));
  }

  createInspection(request: InspectionRequest): Observable<Inspection> {
    return this.http.post<ApiResponse<Inspection>>(this.apiUrl, request)
      .pipe(map(response => response.data));
  }

  resolveInspection(id: number, request: ResolveRequest): Observable<Inspection> {
    return this.http.patch<ApiResponse<Inspection>>(`${this.apiUrl}/${id}/resolve`, request)
      .pipe(map(response => response.data));
  }

  getSummary(): Observable<Summary> {
    return this.http.get<ApiResponse<Summary>>(`${this.apiUrl}/summary`)
      .pipe(map(response => response.data));
  }
}
