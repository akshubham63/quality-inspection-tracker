import { Component, DestroyRef, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { InspectionService } from '../../services/inspection.service';
import { ToastService } from '../../services/toast.service';
import {
  Inspection,
  Severity,
  Status,
  SEVERITIES,
  STATUSES,
  ResolveRequest
} from '../../models/inspection.model';
import { finalize } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-inspection-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './inspection-list.component.html',
  styleUrl: './inspection-list.component.scss'
})
export class InspectionListComponent implements OnInit {
  inspections: Inspection[] = [];
  loading = false;
  
  severities = SEVERITIES;
  statuses = STATUSES;
  
  filters: {
    severity?: Severity;
    status?: Status;
    startDate?: string;
    endDate?: string;
  } = {};

  selectedInspection: Inspection | null = null;
  showResolveModal = false;
  resolutionNote = '';
  resolving = false;
  private readonly destroyRef = inject(DestroyRef);
  constructor(
    private inspectionService: InspectionService,
    private toastService: ToastService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadInspections();
  }

  loadInspections(): void {
    this.loading = true;
    this.inspectionService.getInspections(this.filters)
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        finalize(() => this.loading = false)
      )
      .subscribe({
        next: (inspections) => {
          this.inspections = inspections;
        },
        error: (error) => {
          console.error('Error loading inspections', error);
          this.toastService.error('Failed to load inspections');
        }
      });
  }

  openInspection(inspection: Inspection): void {
    this.selectedInspection = inspection;
    this.showResolveModal = true;
    this.resolutionNote = '';
  }

  closeModal(): void {
    this.showResolveModal = false;
    this.selectedInspection = null;
    this.resolutionNote = '';
  }

  resolveInspection(): void {
    if (!this.selectedInspection || !this.resolutionNote.trim()) return;

    this.resolving = true;
    const request: ResolveRequest = { resolutionNote: this.resolutionNote.trim() };
    
    this.inspectionService.resolveInspection(this.selectedInspection.id, request)
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        finalize(() => this.resolving = false)
      )
      .subscribe({
        next: () => {
          this.toastService.success('Inspection resolved successfully');
          this.closeModal();
          this.loadInspections();
        },
        error: (error) => {
          console.error('Error resolving inspection', error);
          this.toastService.error('Failed to resolve inspection');
        }
      });
  }

  formatDate(date: string): string {
    return new Date(date).toLocaleDateString('en-IN', {
      day: '2-digit',
      month: 'short',
      year: 'numeric'
    });
  }

  formatDateTime(dateTime: string): string {
    return new Date(dateTime).toLocaleString('en-IN', {
      day: '2-digit',
      month: 'short',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}
