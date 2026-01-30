'use client';

import type { Reminder } from '@/lib/types';
import { Card, CardContent } from '@/components/ui/card';
import { Checkbox } from '@/components/ui/checkbox';
import { Button } from '@/components/ui/button';
import { Trash2, CalendarClock } from 'lucide-react';
import { cn } from '@/lib/utils';
import { format, isToday, isTomorrow, isFuture, isPast, startOfToday } from 'date-fns';

type ReminderListProps = {
  reminders: Reminder[];
  onToggle: (id: string) => void;
  onDelete: (id: string) => void;
};

function ReminderItem({
  reminder,
  onToggle,
  onDelete,
}: {
  reminder: Reminder;
  onToggle: (id: string) => void;
  onDelete: (id: string) => void;
}) {
  return (
    <Card className="group/item transition-all duration-300 ease-in-out hover:shadow-lg data-[completed=true]:bg-muted/50 data-[completed=true]:opacity-70" data-completed={reminder.completed}>
      <CardContent className="flex items-center gap-4 p-4">
        <Checkbox
          id={`reminder-${reminder.id}`}
          checked={reminder.completed}
          onCheckedChange={() => onToggle(reminder.id)}
          className="transition-all"
          aria-label={`Mark ${reminder.text} as ${reminder.completed ? 'incomplete' : 'complete'}`}
        />
        <div className="flex-grow grid gap-0.5">
          <label
            htmlFor={`reminder-${reminder.id}`}
            className={cn(
              "cursor-pointer font-medium transition-all duration-300",
              reminder.completed && "line-through text-muted-foreground"
            )}
          >
            {reminder.text}
          </label>
          <p className={cn("text-sm text-muted-foreground transition-all duration-300", reminder.completed && "line-through")}>
            {format(reminder.date, 'p')}
          </p>
        </div>
        <Button variant="ghost" size="icon" className="shrink-0 opacity-0 group-hover/item:opacity-100 transition-opacity" onClick={() => onDelete(reminder.id)} aria-label={`Delete reminder: ${reminder.text}`}>
          <Trash2 className="h-4 w-4 text-muted-foreground hover:text-destructive" />
        </Button>
      </CardContent>
    </Card>
  );
}

function ReminderGroup({ title, reminders, onToggle, onDelete }: ReminderListProps & { title: string }) {
  if (reminders.length === 0) return null;
  return (
    <div className="space-y-4">
      <h2 className="text-lg font-semibold text-muted-foreground tracking-wide">{title}</h2>
      <div className="space-y-2">
        {reminders.map(reminder => (
          <ReminderItem key={reminder.id} reminder={reminder} onToggle={onToggle} onDelete={onDelete} />
        ))}
      </div>
    </div>
  );
}

export function ReminderList({ reminders, onToggle, onDelete }: ReminderListProps) {
  if (reminders.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center text-center py-20">
        <CalendarClock className="w-16 h-16 text-muted-foreground/50 mb-4" />
        <h2 className="text-2xl font-semibold">No Reminders Yet</h2>
        <p className="text-muted-foreground mt-2">Click "New Reminder" to get started.</p>
      </div>
    );
  }

  const todayReminders = reminders.filter(r => isToday(r.date));
  const tomorrowReminders = reminders.filter(r => isTomorrow(r.date));
  const upcomingReminders = reminders.filter(r => isFuture(r.date) && !isToday(r.date) && !isTomorrow(r.date));
  const pastReminders = reminders.filter(r => isPast(r.date) && !isToday(r.date));

  return (
    <div className="container mx-auto px-4 py-8 md:px-6 space-y-8">
      <ReminderGroup title="Today" reminders={todayReminders} onToggle={onToggle} onDelete={onDelete} />
      <ReminderGroup title="Tomorrow" reminders={tomorrowReminders} onToggle={onToggle} onDelete={onDelete} />
      <ReminderGroup title="Upcoming" reminders={upcomingReminders} onToggle={onToggle} onDelete={onDelete} />
      <ReminderGroup title="Past" reminders={pastReminders} onToggle={onToggle} onDelete={onDelete} />
    </div>
  );
}
