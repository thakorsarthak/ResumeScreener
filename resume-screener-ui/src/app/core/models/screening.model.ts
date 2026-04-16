
export interface ScreeningResult {
  id: number;
  sessionId: string;
  matchScore: number;
  matchedSkills: string[];
  missingSkills: string[];
  verdict: 'STRONG MATCH' | 'PARTIAL MATCH' | 'WEAK MATCH';
  jobDescription: string;
  resumeFileName: string;
  screenedAt: string;
}