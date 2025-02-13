package com.uor.eng.cryptx.service;

import com.uor.eng.cryptx.payload.auth.ForgotPasswordRequest;
import com.uor.eng.cryptx.payload.auth.ResetPasswordRequest;
import jakarta.validation.Valid;

public interface PasswordResetService {
  void initiatePasswordReset(@Valid ForgotPasswordRequest forgotPasswordRequest);

  void resetPassword(@Valid ResetPasswordRequest request);
}