import { useState } from 'react'
import { Link } from 'react-router-dom'

export default function Navbar() {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false)

  return (
    <nav style={navbarStyle}>
      <div className="container flex-between">
        <Link to="/" style={logoStyle}>
          <span style={{ fontSize: '1.8rem' }}>📄</span>
          <span style={logoTextStyle}>Resume AI</span>
        </Link>

        <button
          style={mobileToggleStyle}
          onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
          aria-label="Toggle menu"
        >
          <span></span>
          <span></span>
          <span></span>
        </button>

        <div
          style={{
            ...navLinksStyle,
            maxHeight: mobileMenuOpen ? '300px' : '0px',
          }}
        >
          <Link
            to="/"
            style={navLinkStyle}
            onClick={() => setMobileMenuOpen(false)}
          >
            Home
          </Link>
          <Link
            to="/dashboard"
            style={navLinkStyle}
            onClick={() => setMobileMenuOpen(false)}
          >
            Analyze
          </Link>
          <Link
            to="/analysis"
            style={navLinkStyle}
            onClick={() => setMobileMenuOpen(false)}
          >
            Results
          </Link>
        </div>
      </div>
    </nav>
  )
}

const navbarStyle = {
  backgroundColor: '#243746',
  color: 'white',
  padding: '1rem 0',
  position: 'sticky',
  top: 0,
  zIndex: 100,
  boxShadow: '0 2px 8px rgba(36, 55, 70, 0.1)',
}

const logoStyle = {
  display: 'flex',
  alignItems: 'center',
  gap: '0.75rem',
  fontSize: '1.4rem',
  fontWeight: '700',
  color: 'white',
  cursor: 'pointer',
}

const logoTextStyle = {
  fontSize: '1.2rem',
  fontWeight: '700',
}

const navLinksStyle = {
  display: 'flex',
  gap: '2rem',
  alignItems: 'center',
}

const navLinkStyle = {
  color: 'rgba(255, 255, 255, 0.9)',
  fontWeight: '500',
  padding: '0.5rem 1rem',
  borderRadius: '0.5rem',
}

const mobileToggleStyle = {
  display: 'none',
  flexDirection: 'column',
  gap: '5px',
  background: 'none',
  border: 'none',
  cursor: 'pointer',
}