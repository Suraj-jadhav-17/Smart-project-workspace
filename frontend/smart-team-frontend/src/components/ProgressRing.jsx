import React, { useEffect, useState } from 'react'
import './ProgressRing.css'
const ProgressRing = ({ percentage }) => {
  const radius=40;
  const stroke=7;
  const normalizedRadius=radius-stroke*0.5;
  const circumference = normalizedRadius*2*Math.PI;
  const[animated,setAnimated]=useState(false);

  useEffect(()=>setAnimated(true),[]);
  const strockDashoffset= animated?circumference-(percentage/100)*circumference:circumference;

  const circleColor= percentage>70?"green":
                     percentage>40?"orange":
                     "red";
  return(
    <svg height={radius*2} width={radius*2} >
         
         <circle className='ring-bg'
           strokeWidth={stroke}
           r={normalizedRadius}
           cx={radius}
           cy={radius}
         />
         <circle
           className='ring-progress'
           stroke={circleColor}
           strokeWidth={stroke}
           strokeDasharray={`${circumference} ${circumference}`}

           strokeDashoffset={strockDashoffset}
           r={normalizedRadius}
           cx={radius}
           cy={radius}
         />
         <text
         x="50%"
         y="50%"
         dominantBaseline="middle"
         textAnchor='middle'
         fontSize="15"
         >
          {percentage}%
         </text>
    </svg>
  );
};

export default ProgressRing