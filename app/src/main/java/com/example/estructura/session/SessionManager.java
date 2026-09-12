package com.example.estructura.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREFERENCES_NAME =
            "nova_local_session";

    private static final String KEY_USER_ROLE =
            "user_role";

    private final SharedPreferences sharedPreferences;

    public SessionManager(Context context) {
        if (context == null) {
            throw new IllegalArgumentException(
                    "Context no puede ser nulo."
            );
        }

        sharedPreferences = context
                .getApplicationContext()
                .getSharedPreferences(
                        PREFERENCES_NAME,
                        Context.MODE_PRIVATE
                );
    }

    public UserRole getUserRole() {
        String storedRole = sharedPreferences.getString(
                KEY_USER_ROLE,
                UserRole.CLIENTE.name()
        );

        if (storedRole == null) {
            return UserRole.CLIENTE;
        }

        try {
            return UserRole.valueOf(storedRole);
        } catch (IllegalArgumentException exception) {
            return UserRole.CLIENTE;
        }
    }

    public void setUserRole(UserRole userRole) {
        if (userRole == null) {
            throw new IllegalArgumentException(
                    "UserRole no puede ser nulo."
            );
        }

        sharedPreferences
                .edit()
                .putString(KEY_USER_ROLE, userRole.name())
                .apply();
    }

    public void clearSession() {
        sharedPreferences
                .edit()
                .remove(KEY_USER_ROLE)
                .apply();
    }
}