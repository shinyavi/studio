rg# Memento Daily

A reminder/task management web application built with Next.js.

## Tech Stack

- **Framework**: Next.js 15 with App Router
- **Language**: TypeScript
- **Styling**: Tailwind CSS
- **UI Components**: shadcn/ui (Radix UI based)
- **Forms**: React Hook Form + Zod validation
- **Date handling**: date-fns
- **Hosting**: Firebase App Hosting

## Project Structure

```
src/
├── app/                  # Next.js pages
│   ├── page.tsx         # Main home page
│   └── layout.tsx       # Root layout
├── components/
│   ├── ui/              # shadcn/ui components
│   ├── add-reminder-form.tsx
│   ├── reminder-list.tsx
│   └── memento-logo.tsx
├── lib/
│   ├── types.ts         # TypeScript types (Reminder interface)
│   └── utils.ts         # Utility functions (cn helper)
├── hooks/               # Custom React hooks
└── ai/                  # Genkit AI setup (not currently used)
```

## Development

```bash
npm install      # Install dependencies
npm run dev      # Start dev server (http://localhost:3000)
npm run build    # Production build
npm run lint     # Run ESLint
```

## Data Storage

Currently uses browser localStorage only (key: `memento-reminders`). No backend API or database is configured.

## Key Types

```typescript
interface Reminder {
  id: string;
  text: string;
  date: Date;
  completed: boolean;
}
```
