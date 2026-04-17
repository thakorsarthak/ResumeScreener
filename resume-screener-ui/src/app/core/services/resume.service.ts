import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { ScreeningResult } from '../models/screening.model';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environments';

@Injectable({
  providedIn: 'root'
})
export class ResumeService {

  private apiUrl = `${environment.apiUrl}/resume`;

  constructor(private http: HttpClient) {}

  screenResume(file: File, jobDescription: string, sessionId?: string): Observable<ScreeningResult> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('jobDescription', jobDescription);

    let params = new HttpParams();
    if (sessionId) {
      params = params.set('sessionId', sessionId);
    }

    return this.http.post<ScreeningResult>(
      `${this.apiUrl}/screen`, formData, { params }
    );
  }

  getHistory(sessionId: string): Observable<ScreeningResult[]> {
    return this.http.get<ScreeningResult[]>(
      `${this.apiUrl}/history`,
      { params: { sessionId } }
    );
  }
}
