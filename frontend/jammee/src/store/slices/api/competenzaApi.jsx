import { apiSlice } from "../apiSlice";

export const competenzaApi = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        createCompetenza: builder.mutation({
            query: (data) => ({
                url: "/proficiency/create",
                method: "POST",
                body: data,
            }),
            invalidatesTags: (result, error, arg) => {
                const musicianTag = arg?.musicistaId ? { type: "Competenza", id: `LIST-${arg.musicistaId}` } : { type: "Competenza", id: "LIST" };
                return [musicianTag, { type: "Musician", id: "ME" }];
            },
        }),

        updateCompetenza: builder.mutation({
            query: (data) => ({
                url: "/proficiency/update",
                method: "PATCH",
                body: data,
            }),
            invalidatesTags: (result, error, arg) =>
                result
                    ? [
                          { type: "Competenza", id: arg.id },
                          { type: "Competenza", id: arg?.musicistaId ? `LIST-${arg.musicistaId}` : "LIST" },
                      ]
                    : [{ type: "Competenza", id: arg?.musicistaId ? `LIST-${arg.musicistaId}` : "LIST" }],
        }),
        deleteCompetenza: builder.mutation({
            query: (strumentoId) => ({
                url: `/proficiency/delete/${strumentoId}`,
                method: "DELETE",
            }),
            invalidatesTags: (id) => [
                { type: "Competenza", id: "LIST" },
                { type: "Competenza", id },
            ],
        }),
        getCompetenzaByMusicista: builder.query({
            query: (musicistaId) => `/proficiency/user/${musicistaId}`,
            providesTags: (result) =>
                result && result.content ? [...result.content.map((c) => ({ type: "Competenza", id: c.id })), { type: "Competenza", id: "LIST" }] : [{ type: "Competenza", id: "LIST" }],
        }),
    }),
});

export const { useCreateCompetenzaMutation, useUpdateCompetenzaMutation, useDeleteCompetenzaMutation, useGetCompetenzaByMusicistaQuery } = competenzaApi;
