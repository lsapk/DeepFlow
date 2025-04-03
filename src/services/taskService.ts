
import { useIndexedDB } from '@/hooks/use-indexed-db';

// Define the Task type
export interface Task {
  id: string;
  title: string;
  description?: string;
  status: 'todo' | 'in_progress' | 'done';
  priority: 'low' | 'medium' | 'high';
  due_date?: string;
  category?: string;
  completed: boolean;
  created_at: string;
  updated_at: string;
  user_id?: string;
}

// Hook for accessing tasks
export const useTasks = () => {
  return useIndexedDB<Task>({
    storeName: 'tasks',
    initialData: []
  });
};

// Format date for display
export const formatDueDate = (date: string | undefined) => {
  if (!date) return '';
  
  const dueDate = new Date(date);
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  
  const tomorrow = new Date(today);
  tomorrow.setDate(tomorrow.getDate() + 1);
  
  const yesterday = new Date(today);
  yesterday.setDate(yesterday.getDate() - 1);
  
  if (dueDate.getTime() === today.getTime()) {
    return "Aujourd'hui";
  } else if (dueDate.getTime() === tomorrow.getTime()) {
    return "Demain";
  } else if (dueDate.getTime() === yesterday.getTime()) {
    return "Hier";
  }
  
  return dueDate.toLocaleDateString('fr-FR', { 
    day: 'numeric', 
    month: 'short', 
    year: 'numeric' 
  });
};

// Create a new task with current timestamps
export const createTask = (task: Omit<Task, 'id' | 'created_at' | 'updated_at'>): Omit<Task, 'id'> => {
  const now = new Date().toISOString();
  return {
    ...task,
    created_at: now,
    updated_at: now
  };
};

// Update a task with the current timestamp
export const updateTask = (task: Partial<Task> & { id: string }): Partial<Task> => {
  return {
    ...task,
    updated_at: new Date().toISOString()
  };
};
