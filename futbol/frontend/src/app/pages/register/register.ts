import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class Register {
  name = '';
  email = '';
  password = '';
  confirmPassword = '';
  phone = '';
  role: 'COACH' | 'PLAYER' | 'ADMIN' = 'PLAYER';

  errorMessage = '';
  successMessage = '';
  loading = false;

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    if (!this.name || !this.email || !this.password || !this.confirmPassword || !this.phone) {
      this.errorMessage = 'Por favor completa todos los campos';
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Las contraseñas no coinciden';
      return;
    }

    if (this.password.length < 6) {
      this.errorMessage = 'La contraseña debe tener mínimo 6 caracteres';
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.authService.register({
      name: this.name,
      email: this.email,
      password: this.password,
      phone: this.phone,
      role: this.role
    }).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = '¡Registro exitoso! Redirigiendo...';
        setTimeout(() => this.router.navigate(['/login']), 1500);
      },
      error: (err) => {
        this.loading = false;

        const mensaje = err.error?.mensaje || err.error?.message || '';

        if (mensaje.includes('phone') || mensaje.includes('Duplicate')) {
          this.errorMessage = 'El teléfono ya está registrado, usa otro.';
        } else if (mensaje.includes('email') || mensaje.includes('correo')) {
          this.errorMessage = 'El correo ya está registrado, usa otro.';
        } else if (mensaje) {
          this.errorMessage = mensaje;
        } else {
          this.errorMessage = 'Error al registrarse. Intenta de nuevo.';
        }

        console.error('Error registro:', err);
      }
    });
  }
}