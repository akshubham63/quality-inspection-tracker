import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { TOASTER_TYPE } from '../shared/enums/toaster-type.enum';
import { Toast } from '../shared/interfaces/toast.interface';

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  private toasts: Toast[] = [];
  private toastSubject = new BehaviorSubject<Toast[]>([]);
  private counter = 0;

  toasts$ = this.toastSubject.asObservable();

  show(message: string, type: TOASTER_TYPE = TOASTER_TYPE.info): void {
    const toast: Toast = {
      id: ++this.counter,
      message,
      type
    };

    this.toasts.push(toast);
    this.toastSubject.next([...this.toasts]);

    setTimeout(() => {
      this.remove(toast.id);
    }, 3000);
  }

  success(message: string): void {
    this.show(message, TOASTER_TYPE.success);
  }

  error(message: string): void {
    this.show(message, TOASTER_TYPE.error);
  }

  info(message: string): void {
    this.show(message, TOASTER_TYPE.info);
  }

  remove(id: number): void {
    this.toasts = this.toasts.filter(t => t.id !== id);
    this.toastSubject.next([...this.toasts]);
  }
}
