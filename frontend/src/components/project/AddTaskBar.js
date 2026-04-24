import { useState } from "react";

export default function AddTaskBar({ onAdd }) {
    const [value, setValue] = useState("");

    return (
        <div>
            <input
                value={value}
                onChange={(e) => setValue(e.target.value)}
                placeholder="New task"
            />
            <button onClick={() => {
                onAdd(value);
                setValue("");
            }}>
                Add
            </button>
        </div>
    );
}