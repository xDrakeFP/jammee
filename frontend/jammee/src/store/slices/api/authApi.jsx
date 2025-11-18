import { apiSlice } from "../apiSlice";

export const authApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        login: builder.mutation({
            query: (credentials) => ({
                url: "auth/login",
                method: "POST",
                body: credentials,
            }),
        }),

        register: builder.mutation({
            query: (userData) => ({
                url: "/auth/register",
                method: "POST",
                body: userData,
            }),
        }),

        getCurrentUser: builder.query({
            query: () => "/user/me",
            providesTags: ["Auth"],
        }),
    }),
});

export const { useLoginMutation, useRegisterMutation, useGetCurrentUserQuery } = authApi;
