import { apiSlice } from "../apiSlice";

export const authApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        login: builder.mutation({
            query: (credentials) => ({
                url: "auth/login",
                method: "POST",
                body: credentials,
            }),
            providesTags: ["Auth"],
        }),

        register: builder.mutation({
            query: (userData) => ({
                url: "/auth/register",
                method: "POST",
                body: userData,
            }),
            providesTags: ["Auth"],
        }),

        registerMusician: builder.mutation({
            query: (userData) => ({
                url: "/musician/create",
                method: "POST",
                body: userData,
            }),
            providesTags: ["Musicians"],
        }),

        getCurrentUser: builder.query({
            query: () => ({
                url: "/user/me",
                method: "GET",
            }),
            providesTags: ["Auth"],
        }),
    }),
});

export const { useRegisterMusicianMutation, useLoginMutation, useRegisterMutation, useGetCurrentUserQuery } = authApi;
