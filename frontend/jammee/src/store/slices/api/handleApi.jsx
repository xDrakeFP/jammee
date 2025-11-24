export const apiUrl = "http://localhost:3001";
export const getHeaders = () => {
    const headers = {
        "Content-Type": "application/json",
    };

    const token = localStorage.getItem("accessToken");
    if (token) {
        headers.Authorization = "Bearer " + token;
    }
    return headers;
};

export const handleResponse = async (response) => {
    const contentType = response.headers.get("content-type");
    let data = null;

    if (contentType && contentType.includes("application/json")) {
        data = await response.json();
    }
    if (!response.ok) {
        const error = new Error(data?.message || "Errore nella richiesta");
        error.status = response.status;
        error.data = data;
        throw error;
    }
    return data;
};

const normalizeOptions = (options = {}) => {
    const method = (options.method || "GET").toUpperCase();
    const headers = getHeaders();

    let body = options.body;

    const contentType = headers["Content-Type"] || headers["content-type"];
    if (body && typeof body === "object" && contentType && contentType.includes("application/json")) {
        body = JSON.stringify(body);
    }

    return {
        method,
        headers,
        body,
        ...options,
    };
};

export const apiRequest = async (endpoint, options = {}) => {
    const config = normalizeOptions(options);

    try {
        const response = await fetch(`${apiUrl}${endpoint}`, config);
        return await handleResponse(response);
    } catch (error) {
        console.error("API request error:", error);
        throw error;
    }
};
