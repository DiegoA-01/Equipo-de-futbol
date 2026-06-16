export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
  phone: string;
  role: 'COACH' | 'PLAYER' | 'ADMIN';
}

export interface AuthResponse {
  message: string;
  data: {
    jwt: string;       // ← tu API devuelve "jwt" no "token"
    message: string;
  };
}