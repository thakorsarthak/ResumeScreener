import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

   private readonly SESSION_KEY = 'resume_screener_session';

  getSessionId(): string | null {
    return localStorage.getItem(this.SESSION_KEY);
  }

  setSessionId(sessionId: string): void {
    localStorage.setItem(this.SESSION_KEY, sessionId);
  }

  hasSession(): boolean {
    return !!this.getSessionId();
  }

  clearSession(): void {
    localStorage.removeItem(this.SESSION_KEY);
  }
}
