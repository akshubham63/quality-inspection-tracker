import { Component, DestroyRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { InspectionService } from '../../services/inspection.service';
import { ToastService } from '../../services/toast.service';
import {
  InspectionRequest,
  DEFECT_TYPES,
  SEVERITIES
} from '../../models/inspection.model';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-inspection-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: `./inspection-form.component.html`,
  styleUrl: './inspection-form.component.scss'
})
export class InspectionFormComponent {
  defectTypes = DEFECT_TYPES;
  severities = SEVERITIES;
  
  today = new Date().toISOString().split('T')[0];
  
  inspection: Partial<InspectionRequest> = {
    inspectionDate: this.today,
    machineLineId: '',
    defectType: undefined,
    severity: undefined,
    remarks: ''
  };

  submitting = false;
  private readonly destroyRef = inject(DestroyRef);
  constructor(
    private inspectionService: InspectionService,
    private toastService: ToastService,
    private router: Router
  ) {}

  isFormValid(): boolean {
    return !!(
      this.inspection.inspectionDate &&
      this.inspection.machineLineId?.trim() &&
      this.inspection.defectType &&
      this.inspection.severity
    );
  }

  onSubmit(): void {
    if (!this.isFormValid()) return;

    this.submitting = true;
    
    const request: InspectionRequest = {
      inspectionDate: this.inspection.inspectionDate!,
      machineLineId: this.inspection.machineLineId!.trim(),
      defectType: this.inspection.defectType!,
      severity: this.inspection.severity!,
      remarks: this.inspection.remarks?.trim() || undefined
    };

    this.inspectionService.createInspection(request)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.toastService.success('Inspection logged successfully');
          this.router.navigate(['/inspections']);
        },
        error: (error) => {
          console.error('Error creating inspection', error);
          this.toastService.error('Failed to save inspection');
          this.submitting = false;
        }
      });
  }
}
