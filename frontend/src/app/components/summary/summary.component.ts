import { Component, DestroyRef, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InspectionService } from '../../services/inspection.service';
import { ToastService } from '../../services/toast.service';
import { Summary } from '../../models/inspection.model';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-summary',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './summary.component.html',
  styleUrl: './summary.component.scss'
})
export class SummaryComponent implements OnInit {
  summary: Summary | null = null;
  loading = false;
  private readonly destroyRef = inject(DestroyRef);
  constructor(
    private inspectionService: InspectionService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.loadSummary();
  }

  loadSummary(): void {
    this.loading = true;
    this.inspectionService.getSummary()
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        finalize(() => this.loading = false)
      )
      .subscribe({
        next: (summary) => {
          this.summary = summary;
        },
        error: (error) => {
          console.error('Error loading summary', error);
          this.toastService.error('Failed to load summary');
        }
      });
  }

  getResolutionRate(): number {
    if (!this.summary || this.summary.total === 0) return 0;
    return Math.round((this.summary.totalResolved / this.summary.total) * 100);
  }

  getMostCommonSeverity(): string {
    if (!this.summary) return '-';
    
    const severities = [
      { name: 'Critical', total: this.summary.critical.total },
      { name: 'Major', total: this.summary.major.total },
      { name: 'Minor', total: this.summary.minor.total }
    ];

    const max = severities.reduce((prev, current) => 
      (prev.total > current.total) ? prev : current
    );

    return max.total > 0 ? max.name : '-';
  }
}
