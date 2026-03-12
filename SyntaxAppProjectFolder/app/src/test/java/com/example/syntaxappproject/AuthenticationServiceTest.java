package com.example.syntaxappproject;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuthenticationService}.
 * <p>
 * Uses Mockito to mock {@link FirebaseAuth} and {@link FirebaseUser}
 * so tests run without a real Firebase connection.
 * </p>
 */
public class AuthenticationServiceTest {

    private FirebaseAuth mockAuth;
    private FirebaseUser mockUser;
    private AuthenticationService authService;

    /**
     * Initializes mock Firebase objects before each test.
     */
    @Before
    public void setUp() {
        mockAuth = mock(FirebaseAuth.class);
        mockUser = mock(FirebaseUser.class);
    }

    /**
     * Verifies that {@link AuthenticationService#getCurrentUserId()} returns
     * the correct UID when a user is signed in.
     */
    @Test
    public void testGetCurrentUserId_returnsUid_whenUserSignedIn() {
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn("test-uid-001");

        try (MockedStatic<FirebaseAuth> firebaseAuthMock = Mockito.mockStatic(FirebaseAuth.class)) {
            firebaseAuthMock.when(FirebaseAuth::getInstance).thenReturn(mockAuth);
            authService = new AuthenticationService();
            assertEquals("test-uid-001", authService.getCurrentUserId());
        }
    }

    /**
     * Verifies that {@link AuthenticationService#getCurrentUserId()} returns
     * {@code null} when no user is currently signed in.
     */
    @Test
    public void testGetCurrentUserId_returnsNull_whenNoUserSignedIn() {
        when(mockAuth.getCurrentUser()).thenReturn(null);

        try (MockedStatic<FirebaseAuth> firebaseAuthMock = Mockito.mockStatic(FirebaseAuth.class)) {
            firebaseAuthMock.when(FirebaseAuth::getInstance).thenReturn(mockAuth);
            authService = new AuthenticationService();
            assertNull(authService.getCurrentUserId());
        }
    }

    /**
     * Verifies that {@link AuthenticationService#signInAnonymously(AuthenticationService.AuthCallback)}
     * immediately invokes the callback with {@code true} when a user is already signed in.
     */
    @Test
    public void testSignInAnonymously_callsCallbackTrue_whenAlreadySignedIn() {
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);

        try (MockedStatic<FirebaseAuth> firebaseAuthMock = Mockito.mockStatic(FirebaseAuth.class)) {
            firebaseAuthMock.when(FirebaseAuth::getInstance).thenReturn(mockAuth);
            authService = new AuthenticationService();

            boolean[] result = {false};
            authService.signInAnonymously(success -> result[0] = success);

            assertTrue(result[0]);
        }
    }

    /**
     * Verifies that {@link AuthenticationService#signInAnonymously(AuthenticationService.AuthCallback)}
     * does not call Firebase when a user is already signed in.
     */
    @Test
    public void testSignInAnonymously_skipsFirebaseCall_whenAlreadySignedIn() {
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);

        try (MockedStatic<FirebaseAuth> firebaseAuthMock = Mockito.mockStatic(FirebaseAuth.class)) {
            firebaseAuthMock.when(FirebaseAuth::getInstance).thenReturn(mockAuth);
            authService = new AuthenticationService();

            authService.signInAnonymously(success -> {});

            verify(mockAuth, never()).signInAnonymously();
        }
    }

    /**
     * Verifies that {@link AuthenticationService#signOut()} delegates to
     * {@link FirebaseAuth#signOut()} exactly once.
     */
    @Test
    public void testSignOut_callsFirebaseSignOut() {
        when(mockAuth.getCurrentUser()).thenReturn(null);

        try (MockedStatic<FirebaseAuth> firebaseAuthMock = Mockito.mockStatic(FirebaseAuth.class)) {
            firebaseAuthMock.when(FirebaseAuth::getInstance).thenReturn(mockAuth);
            authService = new AuthenticationService();

            authService.signOut();

            verify(mockAuth, times(1)).signOut();
        }
    }
}
