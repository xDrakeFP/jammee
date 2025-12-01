import { apiSlice } from "../apiSlice";

export const messaggiApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        getInbox: builder.query({
            query: () => "/message/inbox",
            providesTags: (result) => (result ? [...(result.content?.map((m) => ({ type: "Messaggio", id: m.id })) ?? []), { type: "Messaggio", id: "LIST" }] : [{ type: "Messaggio", id: "LIST" }]),
        }),

        getMessage: builder.query({
            query: (id) => `/message/${id}`,
            providesTags: (id) => [{ type: "Messaggio", id }],
        }),

        sendMessage: builder.mutation({
            query: ({ destinatarioId, contenuto }) => ({
                url: "/message/send",
                method: "POST",
                body: { destinatarioId, contenuto },
            }),
            invalidatesTags: [{ type: "Messaggio", id: "LIST" }],
        }),

        deleteMessage: builder.mutation({
            query: (id) => ({
                url: `/message/delete/${id}`,
                method: "DELETE",
            }),
            invalidatesTags: (id) => [
                { type: "Messaggio", id },
                { type: "Messaggio", id: "LIST" },
            ],
        }),

        toggleRead: builder.mutation({
            query: (id) => ({
                url: `/message/read/${id}`,
                method: "PATCH",
            }),
            invalidatesTags: (id) => [
                { type: "Messaggio", id },
                { type: "Messaggio", id: "LIST" },
            ],
        }),
    }),
});

export const { useGetInboxQuery, useGetMessageQuery, useSendMessageMutation, useDeleteMessageMutation, useToggleReadMutation } = messaggiApi;
