export default function SkillList({ skills }) {
  if (!skills || skills.length === 0) {
    return (
      <div className="card" style={skillsCardStyle}>
        <h3>Skills Analysis</h3>
        <p style={{ color: '#7E98A4', textAlign: 'center' }}>No skills data available</p>
      </div>
    )
  }

  return (
    <div className="card" style={skillsCardStyle}>
      <h3 style={{ marginBottom: '1.5rem' }}>Skills Analysis</h3>

      <div style={skillsGridStyle}>
        {skills.map((skill, index) => (
          <div key={index} style={skillItemStyle}>
            <div style={skillHeaderStyle}>
              <div>
                <p style={{ fontWeight: '600', margin: '0 0 0.25rem 0', color: '#243746' }}>
                  {skill.name}
                </p>
                <span style={categoryBadgeStyle}>
                  {skill.category}
                </span>
              </div>
              <span style={{
                fontSize: '1.1rem',
                fontWeight: '700',
                color: getMatchColor(skill.match),
              }}>
                {skill.match}%
              </span>
            </div>

            <div className="progress" style={{ margin: '0.75rem 0 0 0' }}>
              <div
                className="progress-bar"
                style={{
                  width: `${skill.match}%`,
                  background: getMatchGradient(skill.match),
                }}
              />
            </div>

            <div style={{
              display: 'flex',
              alignItems: 'center',
              gap: '0.5rem',
              marginTop: '0.5rem',
              fontSize: '0.8rem',
            }}>
              {skill.match >= 90 && <span>✓ Perfect Match</span>}
              {skill.match >= 70 && skill.match < 90 && <span>✓ Strong Match</span>}
              {skill.match >= 50 && skill.match < 70 && <span>⚠ Moderate Match</span>}
              {skill.match < 50 && <span>• Needs Development</span>}
            </div>
          </div>
        ))}
      </div>

      <div style={skillsSummaryStyle}>
        <div style={summaryItemStyle}>
          <p style={{ margin: 0, fontSize: '0.85rem', color: '#7E98A4' }}>Total Skills</p>
          <p style={{ margin: '0.25rem 0 0 0', fontSize: '1.5rem', fontWeight: '700', color: '#243746' }}>
            {skills.length}
          </p>
        </div>

        <div style={summaryItemStyle}>
          <p style={{ margin: 0, fontSize: '0.85rem', color: '#7E98A4' }}>Average Match</p>
          <p style={{ margin: '0.25rem 0 0 0', fontSize: '1.5rem', fontWeight: '700', color: '#243746' }}>
            {Math.round(skills.reduce((sum, s) => sum + s.match, 0) / skills.length)}%
          </p>
        </div>

        <div style={summaryItemStyle}>
          <p style={{ margin: 0, fontSize: '0.85rem', color: '#7E98A4' }}>Strong Skills</p>
          <p style={{ margin: '0.25rem 0 0 0', fontSize: '1.5rem', fontWeight: '700', color: '#243746' }}>
            {skills.filter(s => s.match >= 80).length}
          </p>
        </div>
      </div>
    </div>
  )
}

function getMatchColor(match) {
  if (match >= 90) return '#16a34a'
  if (match >= 70) return '#f59e0b'
  if (match >= 50) return '#3b82f6'
  return '#ef4444'
}

function getMatchGradient(match) {
  if (match >= 90) return 'linear-gradient(90deg, #16a34a, #22c55e)'
  if (match >= 70) return 'linear-gradient(90deg, #f59e0b, #fbbf24)'
  if (match >= 50) return 'linear-gradient(90deg, #3b82f6, #60a5fa)'
  return 'linear-gradient(90deg, #ef4444, #f87171)'
}

const skillsCardStyle = {
  padding: '2rem',
}

const skillsGridStyle = {
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))',
  gap: '1.5rem',
  marginBottom: '2rem',
}

const skillItemStyle = {
  padding: '1.25rem',
  backgroundColor: '#F8F8F6',
  borderRadius: '0.5rem',
  border: '1px solid #E8E8E6',
  transition: 'all 0.3s ease',
  cursor: 'default',
}

const skillHeaderStyle = {
  display: 'flex',
  justifyContent: 'space-between',
  alignItems: 'flex-start',
  gap: '1rem',
}

const categoryBadgeStyle = {
  display: 'inline-block',
  backgroundColor: '#D7E9EE',
  color: '#243746',
  padding: '0.25rem 0.5rem',
  borderRadius: '0.25rem',
  fontSize: '0.75rem',
  fontWeight: '600',
}

const skillsSummaryStyle = {
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fit, minmax(120px, 1fr))',
  gap: '1rem',
  padding: '1.5rem',
  backgroundColor: '#F8F8F6',
  borderRadius: '0.5rem',
  border: '1px solid #E8E8E6',
}

const summaryItemStyle = {
  textAlign: 'center',
}