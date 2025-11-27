import { apiSlice } from "../apiSlice";

export const dimestichezzaApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        createDimestichezza: builder.mutation({
            query: (data) => ({
                url: "/familiarity/create",
                method: "POST",
                body: data,
            }),
            invalidatesTags: [
                { type: "Dimestichezza", id: "LIST" },
                { type: "Musician", id: "ME" },
            ],
        }),

        updateDimestichezza: builder.mutation({
            query: (data) => ({
                url: "/familiarity/update",
                method: "PATCH",
                body: data,
            }),
            invalidatesTags: (result, arg) =>
                result
                    ? [
                          { type: "Dimestichezza", id: arg.id },
                          { type: "Dimestichezza", id: "LIST" },
                      ]
                    : [{ type: "Dimestichezza", id: "LIST" }],
        }),

        deleteDimestichezza: builder.mutation({
            query: (genereId) => ({
                url: `/familiarity/delete/${genereId}`,
                method: "DELETE",
            }),
            invalidatesTags: (id) => [
                { type: "Dimestichezza", id: "LIST" },
                { type: "Dimestichezza", id },
            ],
        }),

        getDimestichezzaByMusicista: builder.query({
            query: (userId) => `/familiarity/user/${userId}`,
            providesTags: (result) =>
                result && result.content ? [...result.content.map((it) => ({ type: "Dimestichezza", id: it.id })), { type: "Dimestichezza", id: "LIST" }] : [{ type: "Dimestichezza", id: "LIST" }],
        }),
    }),
});

export const { useCreateDimestichezzaMutation, useUpdateDimestichezzaMutation, useDeleteDimestichezzaMutation, useGetDimestichezzaByMusicistaQuery } = dimestichezzaApi;
