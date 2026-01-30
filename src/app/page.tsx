'use client';

import { useState, useEffect } from 'react';
import type { Reminder } from '@/lib/types';
import { MementoLogo } from '@/components/memento-logo';
import { ReminderList } from '@/components/reminder-list';
import { AddReminderForm } from '@/components/add-reminder-form';
import { Button } from '@/components/ui/button';
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog';
import { Plus } from 'lucide-react';
import { Skeleton } from '@/components/ui/skeleton';

export default function Home() {
  const [reminders, setReminders] = useState<Reminder[]>([]);
  const [isMounted, setIsMounted] = useState(false);
  const [isDialogOpen, setIsDialogOpen] = useState(false);

  useEffect(() => {
    setIsMounted(true);
    try {
      const storedReminders = localStorage.getItem('memento-reminders');
      if (storedReminders) {
        const parsedReminders = JSON.parse(storedReminders, (key, value) => {
          if (key === 'date' && typeof value === 'string') {
            return new Date(value);
          }
          return value;
        });
        setReminders(parsedReminders);
      }
    } catch (error) {
      console.error('Failed to load reminders from local storage:', error);
      setReminders([]);
    }
  }, []);

  useEffect(() => {
    if (isMounted) {
      try {
        localStorage.setItem('memento-reminders', JSON.stringify(reminders));
      } catch (error) {
        console.error('Failed to save reminders to local storage:', error);
      }
    }
  }, [reminders, isMounted]);

  const addReminder = (text: string, date: Date) => {
    const newReminder: Reminder = {
      id: `${Date.now()}-${Math.random()}`,
      text,
      date,
      completed: false,
    };
    setReminders(prev => [...prev, newReminder].sort((a, b) => a.date.getTime() - b.date.getTime()));
    setIsDialogOpen(false);
  };

  const toggleReminder = (id: string) => {
    setReminders(prev => prev.map(r => r.id === id ? { ...r, completed: !r.completed } : r));
  };

  const deleteReminder = (id: string) => {
    setReminders(prev => prev.filter(r => r.id !== id));
  };

  if (!isMounted) {
    return (
      <div className="flex min-h-screen w-full flex-col">
        <header className="sticky top-0 z-10 border-b bg-background/80 backdrop-blur-sm">
          <div className="container mx-auto flex h-16 items-center justify-between px-4 md:px-6">
            <div className="flex items-center gap-2">
              <Skeleton className="h-8 w-8 rounded-full" />
              <Skeleton className="h-6 w-32" />
            </div>
            <Skeleton className="h-10 w-36 rounded-md" />
          </div>
        </header>
        <main className="flex-1">
          <div className="container mx-auto px-4 py-8 md:px-6">
            <div className="space-y-4">
              <Skeleton className="h-8 w-48" />
              <Skeleton className="h-16 w-full" />
              <Skeleton className="h-16 w-full" />
              <Skeleton className="h-16 w-full" />
            </div>
          </div>
        </main>
      </div>
    );
  }

  return (
    <div className="flex min-h-screen w-full flex-col">
      <header className="sticky top-0 z-10 border-b bg-background/80 backdrop-blur-sm">
        <div className="container mx-auto flex h-16 items-center justify-between px-4 md:px-6">
          <div className="flex items-center gap-2">
            <MementoLogo className="h-8 w-8" />
            <h1 className="text-xl font-bold tracking-tight text-foreground">Memento Daily</h1>
          </div>
          <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
            <DialogTrigger asChild>
              <Button>
                <Plus className="mr-2 h-4 w-4" />
                New Reminder
              </Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-[425px]">
              <DialogHeader>
                <DialogTitle>Add a new reminder</DialogTitle>
                <DialogDescription>What do you need to remember?</DialogDescription>
              </DialogHeader>
              <AddReminderForm onAddReminder={addReminder} />
            </DialogContent>
          </Dialog>
        </div>
      </header>
      <main className="flex-1">
        <ReminderList reminders={reminders} onToggle={toggleReminder} onDelete={deleteReminder} />
      </main>
    </div>
  );
}
