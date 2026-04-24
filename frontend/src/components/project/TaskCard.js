export default function TaskCard({
                                     task,
                                     onComplete,
                                     onAssign,
                                     onDelete,
                                     role
                                 }) {
    return (
        <div style={{ border: "1px solid gray", margin: "5px", padding: "5px" }}>
            {task.name}

            <button onClick={() => onComplete(task.id)}>✔</button>
            <button onClick={() => onAssign(task.id)}>👤</button>

            {role === "ADMIN" && (
                <button onClick={() => onDelete(task.id)}>❌</button>
            )}
        </div>
    );
}