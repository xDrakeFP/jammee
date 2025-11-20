import { createSlice } from "@reduxjs/toolkit";

const initialState = {
    user: JSON.parse(localStorage.getItem("user")) || null,
    token: localStorage.getItem("accessToken") || null,
    isAuthenticated: !!localStorage.getItem("accessToken"),
};

const authSlice = createSlice({
    name: "auth",
    initialState,
    reducers: {
        setCredentials: (state, action) => {
            const { user, token } = action.payload;
            console.log("set Credentials chiamato con", { user, token });

            state.user = user;
            state.token = token;
            state.isAuthenticated = true;

            localStorage.setItem("accessToken", token);
            localStorage.setItem("user", JSON.stringify(user));

            console.log("State aggiornato:", { user: state.user, token: state.token });
        },
        logout: (state) => {
            state.user = null;
            state.token = null;
            state.isAuthenticated = false;

            localStorage.removeItem("accessToken");
            localStorage.removeItem("user");
        },
    },
});

export const { setCredentials, logout } = authSlice.actions;
export default authSlice.reducer;

export const selectCurrentUser = (state) => state.auth.user;
export const selectCurrentToken = (state) => state.auth.token;
export const selectIsAuthenticated = (state) => state.auth.isAuthenticated;
