import { Link } from 'react-router-dom'

export default function Home() {
  return (
    <div>
      {/* Hero Section */}
      <section style={heroStyle}>
        <div className="container">
          <div style={heroContentStyle}>
            <h1 style={heroTitleStyle}>
              Optimize Your Resume for ATS Success
            </h1>
            <p style={heroSubtitleStyle}>
              Get instant AI-powered feedback on your resume's ATS compatibility. 
              Discover your score, identify missing keywords, and land more interviews.
            </p>

            <div style={heroButtonsStyle}>
              <Link to="/dashboard" className="btn btn-primary">
                🚀 Analyze Your Resume
              </Link>
              <button className="btn btn-secondary" style={{ cursor: 'pointer' }}>
                📚 Learn More
              </button>
            </div>

            <div style={heroStatsStyle}>
              <div style={statItemStyle}>
                <p style={{ fontSize: '1.8rem', fontWeight: '700', color: '#243746' }}>500K+</p>
                <p style={{ fontSize: '0.9rem', color: '#7E98A4' }}>Resumes Analyzed</p>
              </div>
              <div style={statItemStyle}>
                <p style={{ fontSize: '1.8rem', fontWeight: '700', color: '#243746' }}>98%</p>
                <p style={{ fontSize: '0.9rem', color: '#7E98A4' }}>User Satisfaction</p>
              </div>
              <div style={statItemStyle}>
                <p style={{ fontSize: '1.8rem', fontWeight: '700', color: '#243746' }}>3x</p>
                <p style={{ fontSize: '0.9rem', color: '#7E98A4' }}>More Interviews</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section style={featuresStyle}>
        <div className="container">
          <h2 style={{ textAlign: 'center', marginBottom: '3rem' }}>
            Powerful Features
          </h2>

          <div style={featuresGridStyle}>
            <FeatureCard
              icon="📊"
              title="ATS Score Analysis"
              description="Get an instant score (0-100) showing how well your resume will perform with Applicant Tracking Systems."
            />
            <FeatureCard
              icon="🎯"
              title="Skill Matching"
              description="See which of your skills match job requirements and get suggestions for improvement."
            />
            <FeatureCard
              icon="💼"
              title="Job Recommendations"
              description="Discover job openings that align with your resume and qualifications."
            />
            <FeatureCard
              icon="🔍"
              title="Detailed Feedback"
              description="Receive actionable suggestions to optimize your resume for better ATS performance."
            />
            <FeatureCard
              icon="⚡"
              title="Instant Results"
              description="Get comprehensive analysis in seconds, not hours. Perfect for quick improvements."
            />
            <FeatureCard
              icon="🔒"
              title="100% Secure"
              description="Your resume data is encrypted and never shared with third parties."
            />
          </div>
        </div>
      </section>

      {/* How It Works */}
      <section style={howItWorksStyle}>
        <div className="container">
          <h2 style={{ textAlign: 'center', marginBottom: '3rem' }}>
            How It Works
          </h2>

          <div style={stepsGridStyle}>
            <Step
              number="1"
              title="Upload Your Resume"
              description="Simply drag and drop your resume or click to select. Supports PDF, DOC, DOCX, and TXT."
            />
            <Step
              number="2"
              title="AI Analysis"
              description="Our advanced AI engine analyzes your resume against modern ATS requirements and job trends."
            />
            <Step
              number="3"
              title="Get Insights"
              description="Receive a detailed ATS score, skill analysis, and personalized recommendations."
            />
            <Step
              number="4"
              title="Optimize & Succeed"
              description="Use our suggestions to improve your resume and get discovered by more employers."
            />
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section style={ctaStyle}>
        <div className="container">
          <div style={ctaContentStyle}>
            <h2 style={{ marginBottom: '1rem', color: 'white' }}>
              Ready to Boost Your Resume?
            </h2>
            <p style={{ marginBottom: '1.5rem', color: 'rgba(255, 255, 255, 0.9)' }}>
              Join thousands of job seekers who've improved their ATS scores with our platform.
            </p>
            <Link to="/dashboard" className="btn btn-primary">
              🚀 Get Started Now
            </Link>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer style={footerStyle}>
        <div className="container">
          <p style={{ margin: 0, color: '#7E98A4', textAlign: 'center' }}>
            © 2024 AI Resume Analyzer. All rights reserved. | 
            <span style={{ marginLeft: '1rem' }}>
              <a href="#" style={{ color: '#243746', textDecoration: 'none' }}>Privacy</a>
              {' '} · {' '}
              <a href="#" style={{ color: '#243746', textDecoration: 'none' }}>Terms</a>
              {' '} · {' '}
              <a href="#" style={{ color: '#243746', textDecoration: 'none' }}>Contact</a>
            </span>
          </p>
        </div>
      </footer>
    </div>
  )
}

function FeatureCard({ icon, title, description }) {
  return (
    <div style={featureCardStyle}>
      <div style={{ fontSize: '2.5rem', marginBottom: '1rem' }}>
        {icon}
      </div>
      <h4 style={{ marginBottom: '0.75rem', color: '#243746' }}>
        {title}
      </h4>
      <p style={{ fontSize: '0.95rem', color: '#7E98A4', margin: 0 }}>
        {description}
      </p>
    </div>
  )
}

function Step({ number, title, description }) {
  return (
    <div style={stepStyle}>
      <div style={stepNumberStyle}>
        {number}
      </div>
      <h4 style={{ marginBottom: '0.75rem', color: '#243746' }}>
        {title}
      </h4>
      <p style={{ fontSize: '0.95rem', color: '#7E98A4', margin: 0 }}>
        {description}
      </p>
    </div>
  )
}

const heroStyle = {
  backgroundColor: '#C7E0E8',
  padding: '5rem 0',
  minHeight: 'calc(100vh - 70px)',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
}

const heroContentStyle = {
  textAlign: 'center',
  maxWidth: '700px',
  margin: '0 auto',
}

const heroTitleStyle = {
  fontSize: '3rem',
  marginBottom: '1rem',
  color: '#243746',
  lineHeight: '1.2',
}

const heroSubtitleStyle = {
  fontSize: '1.1rem',
  color: '#7E98A4',
  marginBottom: '2rem',
  lineHeight: '1.7',
}

const heroButtonsStyle = {
  display: 'flex',
  gap: '1rem',
  justifyContent: 'center',
  marginBottom: '3rem',
  flexWrap: 'wrap',
}

const heroStatsStyle = {
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fit, minmax(150px, 1fr))',
  gap: '2rem',
  marginTop: '2rem',
  padding: '2rem',
  backgroundColor: 'rgba(255, 255, 255, 0.5)',
  borderRadius: '1rem',
}

const statItemStyle = {
  textAlign: 'center',
}

const featuresStyle = {
  padding: '5rem 0',
  backgroundColor: '#F8F8F6',
}

const featuresGridStyle = {
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))',
  gap: '2rem',
}

const featureCardStyle = {
  padding: '2rem',
  backgroundColor: '#D7E9EE',
  borderRadius: '0.75rem',
  textAlign: 'center',
  transition: 'all 0.3s ease',
}

const howItWorksStyle = {
  padding: '5rem 0',
  backgroundColor: '#F8F8F6',
}

const stepsGridStyle = {
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))',
  gap: '2rem',
}

const stepStyle = {
  padding: '2rem',
  backgroundColor: '#D7E9EE',
  borderRadius: '0.75rem',
  textAlign: 'center',
}

const stepNumberStyle = {
  width: '50px',
  height: '50px',
  borderRadius: '50%',
  backgroundColor: '#243746',
  color: 'white',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  fontSize: '1.5rem',
  fontWeight: '700',
  margin: '0 auto 1rem',
}

const ctaStyle = {
  backgroundColor: '#243746',
  color: 'white',
  padding: '4rem 0',
  textAlign: 'center',
}

const ctaContentStyle = {
  maxWidth: '500px',
  margin: '0 auto',
}

const footerStyle = {
  backgroundColor: '#D7E9EE',
  padding: '2rem 0',
  borderTop: '1px solid #9FB9C2',
}