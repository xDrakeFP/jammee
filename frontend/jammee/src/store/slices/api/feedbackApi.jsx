import { apiSlice } from "../apiSlice";

export const feedbackApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        createFeedback: builder.mutation({
            query: (data) => ({
                url: "/feedback/create",
                method: "POST",
                body: data,
            }),
            invalidatesTags: [
                { type: "Feedback", id: "LIST" },
                { type: "Musician", id: "ME" },
            ],
        }),

        deleteFeedback: builder.mutation({
            query: (id) => ({
                url: `/feedback/${id}`,
                method: "DELETE",
            }),
            invalidatesTags: (result, arg) => [
                { type: "Feedback", id: "LIST" },
                { type: "Feedback", id: arg },
            ],
        }),

        getFeedbackByRecipient: builder.query({
            query: (recipientId) => `/feedback/by-user/${recipientId}`,
            providesTags: (result) =>
                result && result.content ? [...result.content.map((f) => ({ type: "Feedback", id: f.id })), { type: "Feedback", id: "LIST" }] : [{ type: "Feedback", id: "LIST" }],
        }),
    }),
});

export const { useCreateFeedbackMutation, useDeleteFeedbackMutation, useGetFeedbackByRecipientQuery } = feedbackApi;
