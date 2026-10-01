# Memento Daily - Data Flow Diagram

```mermaid
flowchart TB
    subgraph User["👤 User Actions"]
        A[Open App]
        B[Click 'New Reminder']
        C[Fill Form & Submit]
        D[Toggle Checkbox]
        E[Click Delete]
    end

    subgraph Browser["🌐 Browser Storage"]
        LS[(localStorage<br/>'memento-reminders')]
    end

    subgraph App["⚛️ React Application"]
        subgraph PageComponent["page.tsx (Main Controller)"]
            State[("useState<br/>reminders[]")]
            Load["useEffect #1<br/>Load from localStorage"]
            Save["useEffect #2<br/>Save to localStorage"]
            AddFn["addReminder()"]
            ToggleFn["toggleReminder()"]
            DeleteFn["deleteReminder()"]
        end

        subgraph Form["AddReminderForm"]
            FormState["react-hook-form<br/>state"]
            Validate["Zod Schema<br/>Validation"]
            Submit["onSubmit()"]
        end

        subgraph List["ReminderList"]
            Filter["Filter by date:<br/>Today/Tomorrow/<br/>Upcoming/Past"]
            Render["Render<br/>ReminderItems"]
        end
    end

    %% Initial Load Flow
    A -->|"1. App mounts"| Load
    Load -->|"2. getItem()"| LS
    LS -->|"3. JSON.parse()"| State

    %% Add Reminder Flow
    B -->|"4. Opens Dialog"| FormState
    C -->|"5. Input"| FormState
    FormState -->|"6. Validate"| Validate
    Validate -->|"7. Valid"| Submit
    Submit -->|"8. onAddReminder()"| AddFn
    AddFn -->|"9. setReminders()"| State

    %% Display Flow
    State -->|"10. props"| Filter
    Filter -->|"11. grouped"| Render

    %% Toggle/Delete Flow
    D -->|"12. onToggle(id)"| ToggleFn
    E -->|"13. onDelete(id)"| DeleteFn
    ToggleFn -->|"14. setReminders()"| State
    DeleteFn -->|"15. setReminders()"| State

    %% Save Flow
    State -->|"16. State changes"| Save
    Save -->|"17. setItem()"| LS

    %% Styling
    classDef storage fill:#f9d77e,stroke:#d4a017,color:#000
    classDef component fill:#a8d5ff,stroke:#3182ce,color:#000
    classDef action fill:#c6f6d5,stroke:#38a169,color:#000
    classDef state fill:#fed7e2,stroke:#d53f8c,color:#000

    class LS storage
    class State,FormState state
    class AddFn,ToggleFn,DeleteFn,Submit,Load,Save action
    class Filter,Render,Validate component
```

## Data Flow Summary

| Step | What Happens |
|------|--------------|
| **1-3** | App loads → Reads localStorage → Populates React state |
| **4-9** | User adds reminder → Form validates → Updates state |
| **10-11** | State passes to ReminderList → Filters & displays |
| **12-15** | User toggles/deletes → Handler updates state |
| **16-17** | Any state change → Auto-saves to localStorage |

## Key Points

- **Single source of truth**: `reminders[]` state in `page.tsx`
- **Unidirectional flow**: Data flows down, actions flow up
- **Auto-persistence**: Every state change triggers localStorage save
- **No network calls**: Everything happens locally in the browser
