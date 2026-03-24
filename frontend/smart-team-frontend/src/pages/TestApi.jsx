import { use, useEffect,useState } from "react";
import api from "../api/api";

function TestAPI() {
  
  const [user, setUser] = useState(null);
  const [error, setError] = useState(null);

useEffect(() => {
    api.get("/users/1")
        .then((response)=>{
            setUser(response.data.data);
        }).catch((err)=>{
            console.error(err);
            setError(err.message);
        })
        
},[]);
  if (error) return <p style={{ color: "red" }}>Error: {error}</p>;
  if (!user) return <p>Loading...</p>;
const {userName,email,password,accountStatus,role,userId}=user;
  return<>
   <div style={{ padding: "20px" }}>
      <h1>Test API Data</h1>
      <h1>{userId}</h1>
    <h1>{userName}</h1>
    <p>{email}</p>
    <p>{password}</p>
    <p>{accountStatus}</p>
    <h1>{role}</h1>
    {/* <p>{teamMemberships}</p> */}
    </div>
  </>;
}

export default TestAPI;