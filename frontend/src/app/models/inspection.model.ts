export interface Inspection {
  id: number;
  inspectionDate: string;
  machineLineId: string;
  defectType: DefectType;
  defectTypeDisplayName: string;
  severity: Severity;
  remarks?: string;
  status: Status;
  resolutionNote?: string;
  resolvedAt?: string;
  createdAt: string;
  updatedAt: string;
}

export interface InspectionRequest {
  inspectionDate: string;
  machineLineId: string;
  defectType: DefectType;
  severity: Severity;
  remarks?: string;
}

export interface ResolveRequest {
  resolutionNote: string;
}

export interface Summary {
  critical: SeveritySummary;
  major: SeveritySummary;
  minor: SeveritySummary;
  totalOpen: number;
  totalResolved: number;
  total: number;
}

export interface SeveritySummary {
  open: number;
  resolved: number;
  total: number;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
}

export type DefectType = 'WEAVE_DEFECT' | 'SHADE_VARIATION' | 'HOLE_TEAR' | 'COUNT_DEVIATION' | 'OTHER';
export type Severity = 'CRITICAL' | 'MAJOR' | 'MINOR';
export type Status = 'OPEN' | 'RESOLVED';

export const DEFECT_TYPES: { value: DefectType; label: string }[] = [
  { value: 'WEAVE_DEFECT', label: 'Weave Defect' },
  { value: 'SHADE_VARIATION', label: 'Shade Variation' },
  { value: 'HOLE_TEAR', label: 'Hole/Tear' },
  { value: 'COUNT_DEVIATION', label: 'Count Deviation' },
  { value: 'OTHER', label: 'Other' }
];

export const SEVERITIES: { value: Severity; label: string }[] = [
  { value: 'CRITICAL', label: 'Critical' },
  { value: 'MAJOR', label: 'Major' },
  { value: 'MINOR', label: 'Minor' }
];

export const STATUSES: { value: Status; label: string }[] = [
  { value: 'OPEN', label: 'Open' },
  { value: 'RESOLVED', label: 'Resolved' }
];
