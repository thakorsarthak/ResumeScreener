import { Component } from '@angular/core';
import { ScreeningResult } from '../../core/models/screening.model';
import { ResumeService } from '../../core/services/resume.service';
import { SessionService } from '../../core/services/session.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FileUploadModule } from 'primeng/fileupload';
import { InputTextarea } from 'primeng/inputtextarea';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ChipModule } from 'primeng/chip';
import { TagModule } from 'primeng/tag';
import { MessagesModule } from 'primeng/messages';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-screener',
  imports: [CommonModule, FormsModule,
    FileUploadModule, InputTextarea,
    ButtonModule, CardModule, ProgressSpinnerModule,
    ChipModule, TagModule, MessagesModule],
  templateUrl: './screener.component.html',
  styleUrl: './screener.component.scss'
})
export class ScreenerComponent {

  selectedFile: File | null = null;
  jobDescription: string = '';
  isLoading: boolean = false;
  result: ScreeningResult | null = null;
  errorMessage: string = '';

   constructor(
    private resumeService: ResumeService,
    public sessionService: SessionService,
    private router: Router
  ) {}

  onFileSelect(event: any): void {
    this.selectedFile = event.files[0];
    this.errorMessage = '';
  }


  onFileRemove(): void {
    this.selectedFile = null;
  }


  canSubmit(): boolean {
    return !!this.selectedFile && this.jobDescription.trim().length > 20 && !this.isLoading;
  }

  screenResume(): void {
    if (!this.canSubmit())
       return;

    this.isLoading = true;
    this.result = null;
    this.errorMessage = '';
  
  const sessionId = this.sessionService.getSessionId() || undefined;
  
  this.resumeService.screenResume(this.selectedFile!, this.jobDescription, sessionId).subscribe({
      next: (result) => {
        this.result = result;
        // Save sessionId from first screening
        this.sessionService.setSessionId(result.sessionId);
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Screening failed', error);
        this.errorMessage = 'Failed to screen resume. Please try again.';
        this.isLoading = false;
       }
    });
  }

  getVerdictSeverity(verdict: string): string {
    switch(verdict) {
      case 'STRONG MATCH': return 'success';
      case 'PARTIAL MATCH': return 'warning';
      case 'WEAK MATCH': return 'danger';
      default: return 'info';
    }
  }

  viewHistory(): void {
    this.router.navigate(['/history']);
  }


}
