import api from "../../api/api";

api
export const getCommentsByTaskId= async (taskId) => {
  try {
    const response = await api.get(`/comments/task/${taskId}`); 
    return response.data.data;;
  } catch (error) {
    console.error("Error fetching comments:", error);
    throw error;
  }
};

