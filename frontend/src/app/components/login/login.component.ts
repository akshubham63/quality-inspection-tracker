import { Component, DestroyRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ToastService } from '../../services/toast.service';
import { LoginRequest, RegisterRequest } from '../../models/auth.model';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { finalize } from 'rxjs';
import { TOASTER_TYPE } from '../../shared/enums/toaster-type.enum';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: `./login.component.html`,
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  isRegisterMode = false;
  loading = false;
  error = '';

  loginData: LoginRequest = {
    username: '',
    password: ''
  };

  registerData: RegisterRequest = {
    username: '',
    password: '',
    fullName: ''
  };
    private readonly destroyRef = inject(DestroyRef);
  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private toastService: ToastService
  ) {}

  toggleMode(): void {
    this.isRegisterMode = !this.isRegisterMode;
    this.error = '';
  }

  onSubmit(): void {
    this.loading = true;
    this.error = '';

    if (this.isRegisterMode) {
      this.register();
    } else {
      this.login();
    }
  }

  private login(): void {
    this.authService.login(this.loginData)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (response) => {
          this.loading = false;
          if (response.success) {
            this.toastService.show('Welcome back!', TOASTER_TYPE.success);
            this.redirectAfterAuth();
          } else {
            this.error = response.message || 'Login failed';
          }
        },
        error: (err) => {
          this.loading = false;
          this.error = err.error?.message || 'Invalid username or password';
        }
      });
  }

  private register(): void {
    const data: RegisterRequest = {
      ...this.registerData,
      username: this.loginData.username,
      password: this.loginData.password
    };

    this.authService.register(data)
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        finalize(() => this.loading = false)
      )
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.toastService.show('Account created successfully!', TOASTER_TYPE.success);
            this.redirectAfterAuth();
          } else {
            this.error = response.message || 'Registration failed';
          }
        },
        error: (err) => {
          this.error = err.error?.message || 'Registration failed';
        }
      });
  }

  private redirectAfterAuth(): void {
    const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/inspections';
    this.router.navigateByUrl(returnUrl);
  }
}
