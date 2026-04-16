import { Component } from '@angular/core';
import { ScreeningResult } from '../../core/models/screening.model';
import { ResumeService } from '../../core/services/resume.service';
import { SessionService } from '../../core/services/session.service';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { ChipModule } from 'primeng/chip';

@Component({
  selector: 'app-history',
  imports: [
    CommonModule, RouterLink,
    TableModule, TagModule,
    ButtonModule, CardModule, ChipModule
  ],
  templateUrl: './history.component.html',
  styleUrl: './history.component.scss'
})
export class HistoryComponent {

  history: ScreeningResult[] = [];
  isLoading: boolean = true;
  noSession: boolean = false;

  constructor(
    private resumeService: ResumeService,
    private sessionService: SessionService
  ) { }

  ngOnInit(): void {
    const sessionId = this.sessionService.getSessionId();

    if (!sessionId) {
      this.noSession = true;
      this.isLoading = false;
      return;
    }

    this.resumeService.getHistory(sessionId).subscribe({
      next: (data) => {
        this.history = data;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }
  getVerdictSeverity(verdict: string): string {
    switch (verdict) {
      case 'STRONG MATCH': return 'success';
      case 'PARTIAL MATCH': return 'warning';
      case 'WEAK MATCH': return 'danger';
      default: return 'info';
    }
  }


}
