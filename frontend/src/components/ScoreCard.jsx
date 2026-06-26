export default function ScoreCard({ atsScore, rating, fileName, uploadedAt }) {
  const getScoreColor = (score) => {
    if (score >= 80) return '#16a34a'
    if (score >= 60) return '#f59e0b'
    return '#dc2626'
  }

  const getRatingText = (score) => {
    if (score >= 80) return 'Excellent'
    if (score >= 60) return 'Good'
    return 'Needs Improvement'
  }

  const scoreColor = getScoreColor(atsScore)
  const scoreRadius = 60
  const scoreCircumference = 2 * Math.PI * scoreRadius

  return (
    <div className="card" style={scoreCardStyle}>
      <h3 style={{ marginBottom: '1.5rem', textAlign: 'center' }}>ATS Score</h3>

      <div style={circleContainerStyle}>
        <svg
          width="200"
          height="200"
          style={{ transform: 'rotate(-90deg)' }}
        >
          <circle
            cx="100"
            cy="100"
            r={scoreRadius}
            fill="none"
            stroke="#E8E8E6"
            strokeWidth="8"
          />
          <circle
            cx="100"
            cy="100"
            r={scoreRadius}
            fill="none"
            stroke={scoreColor}
            strokeWidth="8"
            strokeDasharray={scoreCircumference}
            strokeDashoffset={scoreCircumference - (atsScore / 100) * scoreCircumference}
            strokeLinecap="round"
            style={{ transition: 'stroke-dashoffset 0.8s ease' }}
          />
        </svg>

        <div style={scoreTextStyle}>
          <div style={{ fontSize: '3rem', fontWeight: '700', color: scoreColor }}>
            {atsScore}
          </div>
          <div style={{ fontSize: '0.9rem', color: '#7E98A4' }}>/ 100</div>
        </div>
      </div>

      <div style={{ textAlign: 'center', marginTop: '1.5rem' }}>
        <p style={{ margin: '0.5rem 0' }}>
          <span style={{
            backgroundColor: scoreColor,
            color: 'white',
            padding: '0.375rem 0.75rem',
            borderRadius: '0.25rem',
            fontSize: '0.85rem',
            fontWeight: '600',
          }}>
            {getRatingText(atsScore)}
          </span>
        </p>
        {fileName && (
          <p style={{ fontSize: '0.85rem', color: '#7E98A4', margin: '0.5rem 0' }}>
            📄 {fileName}
          </p>
        )}
        {uploadedAt && (
          <p style={{ fontSize: '0.85rem', color: '#7E98A4', margin: '0.5rem 0' }}>
            📅 {uploadedAt}
          </p>
        )}
      </div>

      <div style={recommendationStyle}>
        <p style={{ fontWeight: '600', marginBottom: '0.5rem', color: '#243746' }}>
          💡 What This Means
        </p>
        {atsScore >= 80 && (
          <p style={{ fontSize: '0.9rem', color: '#7E98A4' }}>
            Your resume is well-optimized for ATS systems. You're likely to pass initial screening.
          </p>
        )}
        {atsScore >= 60 && atsScore < 80 && (
          <p style={{ fontSize: '0.9rem', color: '#7E98A4' }}>
            Your resume is decent but has room for improvement. Consider optimizing keywords and formatting.
          </p>
        )}
        {atsScore < 60 && (
          <p style={{ fontSize: '0.9rem', color: '#7E98A4' }}>
            Your resume needs significant improvements. Review the suggestions below to boost your score.
          </p>
        )}
      </div>
    </div>
  )
}

const scoreCardStyle = {
  maxWidth: '400px',
  margin: '0 auto',
  textAlign: 'center',
  padding: '2rem',
}

const circleContainerStyle = {
  position: 'relative',
  width: '200px',
  height: '200px',
  margin: '0 auto',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
}

const scoreTextStyle = {
  position: 'absolute',
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  justifyContent: 'center',
}

const recommendationStyle = {
  marginTop: '1.5rem',
  padding: '1rem',
  backgroundColor: '#F8F8F6',
  borderRadius: '0.5rem',
  border: '1px solid #E8E8E6',
}