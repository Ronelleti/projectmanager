export default function TaskHeader({ role, onLogout }) {
    return (
        <div>
            <h2>Dashboard ({role})</h2>
            <button onClick={onLogout}>Logout</button>
        </div>
    );
}