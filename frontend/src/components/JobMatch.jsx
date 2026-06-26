export default function JobMatch({ jobMatches }) {
  if (!jobMatches || jobMatches.length === 0) {
    return (
      <div className="card" style={jobMatchCardStyle}>
        <h3>Recommended Job Matches</h3>
        <p style={{ color: '#7E98A4', textAlign: 'center' }}>No job matches available</p>
      </div>
    )
  }

  return (
    <div className="card" style={jobMatchCardStyle}>
      <h3 style={{ marginBottom: '1.5rem' }}>🎯 Recommended Job Matches</h3>

      <div style={jobsListStyle}>
        {jobMatches.map((job) => (
          <div key={job.id} style={jobItemStyle}>
            <div style={jobHeaderStyle}>
              <div>
                <h4 style={{ margin: '0 0 0.5rem 0', color: '#243746' }}>
                  {job.title}
                </h4>
                <p style={{ margin: '0', color: '#7E98A4', fontSize: '0.9rem' }}>
                  {job.company}
                </p>
              </div>
              <div style={matchPercentageStyle}>
                <div style={matchCircleStyle(job.matchPercentage)}>
                  <span style={{ fontWeight: '700', color: 'white' }}>
                    {job.matchPercentage}%
                  </span>
                </div>
              </div>
            </div>

            <div style={jobBodyStyle}>
              <div style={salaryStyle}>
                <span style={{ fontSize: '0.85rem', color: '#7E98A4' }}>💰 Salary Range</span>
                <p style={{ margin: '0.25rem 0 0 0', fontWeight: '600', color: '#243746' }}>
                  {job.salary}
                </p>
              </div>

              <div style={skillsMatchStyle}>
                <span style={{ fontSize: '0.85rem', color: '#7E98A4' }}>✓ Key Skills Match</span>
                <div style={skillTagsStyle}>
                  {job.skills.map((skill, idx) => (
                    <span key={idx} style={skillTagStyle}>
                      {skill}
                    </span>
                  ))}
                </div>
              </div>
            </div>

            <button style={applyButtonStyle}>
              View Job Details →
            </button>
          </div>
        ))}
      </div>

      <div style={matchInfoStyle}>
        <p style={{ margin: 0, fontSize: '0.9rem', color: '#7E98A4' }}>
          💡 Match percentage is based on your resume's alignment with job requirements.
          Higher percentages indicate better skill alignment.
        </p>
      </div>
    </div>
  )
}

function matchCircleStyle(percentage) {
  let backgroundColor = '#16a34a'
  if (percentage < 70) backgroundColor = '#f59e0b'
  if (percentage < 50) backgroundColor = '#ef4444'

  return {
    width: '70px',
    height: '70px',
    borderRadius: '50%',
    backgroundColor,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    fontSize: '0.9rem',
    color: 'white',
    fontWeight: '700',
  }
}

const jobMatchCardStyle = {
  padding: '2rem',
}

const jobsListStyle = {
  display: 'grid',
  gap: '1.5rem',
  marginBottom: '1.5rem',
}

const jobItemStyle = {
  padding: '1.5rem',
  backgroundColor: '#F8F8F6',
  borderRadius: '0.75rem',
  border: '1px solid #E8E8E6',
  transition: 'all 0.3s ease',
}

const jobHeaderStyle = {
  display: 'flex',
  justifyContent: 'space-between',
  alignItems: 'flex-start',
  gap: '1rem',
  marginBottom: '1rem',
}

const matchPercentageStyle = {
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
}

const jobBodyStyle = {
  display: 'grid',
  gap: '1rem',
  marginBottom: '1rem',
}

const salaryStyle = {
  paddingBottom: '1rem',
  borderBottom: '1px solid #E8E8E6',
}

const skillsMatchStyle = {
}

const skillTagsStyle = {
  display: 'flex',
  flexWrap: 'wrap',
  gap: '0.5rem',
  marginTop: '0.5rem',
}

const skillTagStyle = {
  display: 'inline-block',
  backgroundColor: '#D7E9EE',
  color: '#243746',
  padding: '0.375rem 0.75rem',
  borderRadius: '0.25rem',
  fontSize: '0.8rem',
  fontWeight: '500',
}

const applyButtonStyle = {
  width: '100%',
  padding: '0.875rem 1.5rem',
  backgroundColor: '#243746',
  color: 'white',
  border: 'none',
  borderRadius: '0.5rem',
  fontWeight: '600',
  cursor: 'pointer',
  transition: 'all 0.3s ease',
}

const matchInfoStyle = {
  padding: '1rem',
  backgroundColor: '#F8F8F6',
  borderRadius: '0.5rem',
  border: '1px solid #E8E8E6',
}