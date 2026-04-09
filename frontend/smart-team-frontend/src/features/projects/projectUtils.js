export const groupTasksByProject = (tasks) => {
  return tasks.reduce((acc, task) => {
    const projectId = String(task.project?.id || task.projectId);

    if (!projectId) return acc;

    if (!acc[projectId]) {
      acc[projectId] = [];
    }

    acc[projectId].push(task);

    return acc;
  }, {});
};